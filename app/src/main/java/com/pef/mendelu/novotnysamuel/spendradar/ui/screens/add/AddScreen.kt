package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptDraftStore
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.permissions.LocationPermissionHandler
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddAddItem
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddItemNamePrefix
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddItemPricePrefix
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddItemQuantityPrefix
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddSaveButton
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddStoreName
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BottomNavBar
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.MapPicker
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.categoryLabel
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.accentColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.barColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.basicMargin
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.darkGreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.halfMargin
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.lightButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textFieldColors
import com.pef.mendelu.novotnysamuel.spendradar.utils.areItemsValid
import com.pef.mendelu.novotnysamuel.spendradar.utils.getLastKnownLocation
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AddScreen(
    navigation: INavigationRouter,
    receiptId: Long? = null,
    fromCamera: Boolean = false
) {
    val viewModel = hiltViewModel<AddViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(receiptId) {
        if (receiptId != null && receiptId != -1L) {
            viewModel.loadReceipt(receiptId)
        }
    }

    LaunchedEffect(fromCamera) {
        if (fromCamera) {
            val draft = ReceiptDraftStore.consume()
            if (draft != null) {
                viewModel.applyDraft(draft)
            }
        }
    }

    LaunchedEffect(state.saveCompleted) {
        if (state.saveCompleted) {
            if (fromCamera) {
                navigation.navigateToList()
            } else {
                navigation.returnBack()
            }
            viewModel.onSaveHandled()
        }
    }

    BaseScreen(
        topBarText = if (receiptId != null && receiptId != -1L) {
            stringResource(R.string.edit_receipt)
        } else {
            stringResource(R.string.new_receipt)
        },
        onBackClick = { navigation.returnBack() },
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.ListScreen,
                navigation = navigation
            )
        }
    ) { paddingValues ->
        AddScreenContent(
            paddingValues = paddingValues,
            state = state,
            actions = viewModel,
            isEditing = receiptId != null && receiptId != -1L
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun AddScreenContent(
    paddingValues: PaddingValues,
    state: AddUIState,
    actions: AddActions,
    isEditing: Boolean
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var isMapInteracting by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var locationInitialized by remember { mutableStateOf(false) }

    val dateText = if (state.dateMillis == null) {
        stringResource(R.string.add_date_placeholder)
    } else {
        val date = Instant.ofEpochMilli(state.dateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        date.format(DateTimeFormatter.ofPattern("d. M. yyyy", Locale.getDefault()))
    }

    val timeText = state.time.format(DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()))

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.dateMillis ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = barButton),
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    actions.onDateChange(datePickerState.selectedDateMillis)
                    showDatePicker = false
                    showTimePicker = true
                }) {
                    Text(text = stringResource(android.R.string.ok), color = accentColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = stringResource(R.string.cancel), color = textColor)
                }
            }
        ) {
            CompositionLocalProvider(LocalContentColor provides textColor){
            DatePicker(state = datePickerState, colors = DatePickerDefaults.colors(
                containerColor = barButton,
                titleContentColor = textColor,
                headlineContentColor = textColor,
                weekdayContentColor = textColor,
                dayContentColor = textColor,
                selectedDayContainerColor = accentColor,
                selectedDayContentColor = barColor,
                todayContentColor = accentColor,
                todayDateBorderColor = accentColor,
                dividerColor = accentColor,
                subheadContentColor = textColor,
                selectedYearContainerColor = accentColor,
                selectedYearContentColor = textColor,
                dateTextFieldColors = textFieldColors(),
                navigationContentColor = textColor
            ))}
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = state.time.hour,
            initialMinute = state.time.minute,
            is24Hour = true
        )
        AlertDialog(
            containerColor = barButton,
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    actions.onTimeChange(LocalTime.of(timePickerState.hour, timePickerState.minute))
                    showTimePicker = false
                }) {
                    Text(text = stringResource(android.R.string.ok), color = accentColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(text = stringResource(R.string.cancel), color = textColor)
                }
            },
            text = {
                TimePicker(state = timePickerState, colors = TimePickerDefaults.colors(
                    containerColor = barButton,
                    clockDialColor = darkGreen,
                    clockDialSelectedContentColor = barColor,
                    selectorColor = accentColor,
                    periodSelectorSelectedContainerColor = barColor,
                    periodSelectorSelectedContentColor = textColor,
                    periodSelectorUnselectedContainerColor = barColor,
                    periodSelectorUnselectedContentColor = textColor,
                    timeSelectorSelectedContainerColor = accentColor,
                    timeSelectorUnselectedContainerColor = barColor,
                /*
                periodSelectorBorderColor: Color = Color.Unspecified,
                : Color = Color.Unspecified,
                periodSelectorUnselectedContainerColor: Color = Color.Unspecified,
                periodSelectorSelectedContentColor: Color = Color.Unspecified,
                periodSelectorUnselectedContentColor: Color = Color.Unspecified,
                timeSelectorSelectedContainerColor: Color = Color.Unspecified,
                timeSelectorUnselectedContainerColor: Color = Color.Unspecified,
                timeSelectorSelectedContentColor: Color = Color.Unspecified,
                timeSelectorUnselectedContentColor: Color = Color.Unspecified
                */
                ))
            }
        )
    }

    LocationPermissionHandler(
        onPermissionGranted = { hasLocationPermission = true },
        onPermissionDenied = { hasLocationPermission = false }
    )

    LaunchedEffect(hasLocationPermission, isEditing, state.location) {
        if (hasLocationPermission &&
            !isEditing &&
            !locationInitialized &&
            isDefaultLocation(state.location)
        ) {
            val location = getLastKnownLocation(context)
            if (location != null) {
                actions.onLocationChange(LatLng(location.latitude, location.longitude))
                locationInitialized = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState, enabled = !isMapInteracting)
            .padding(horizontal = basicMargin, vertical = halfMargin),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.storeName,
            onValueChange = actions::onStoreNameChange,
            label = { Text(stringResource(R.string.store_name)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTagAddStoreName),
            colors = textFieldColors(),
            singleLine = true,
            maxLines = 1
        )

        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
        ) {
            OutlinedTextField(
                value = categoryLabel(state.category),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.category)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = textFieldColors(),
                singleLine = true,
                maxLines = 1
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                ReceiptCategory.values().forEach { category ->
                    DropdownMenuItem(
                        text = { Text(text = categoryLabel(category)) },
                        onClick = {
                            actions.onCategoryChange(category)
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = "$dateText • $timeText",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.date_time)) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                colors = textFieldColors(),
                singleLine = true,
                maxLines = 1
            )
            TextButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(text = stringResource(R.string.pick_date_time), color = accentColor)
            }
        }

        Text(
            text = stringResource(R.string.purchase_location),
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )

        Surface(
            color = barColor,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, barButton),
            modifier = Modifier.fillMaxWidth()
        ) {
            MapPicker(
                location = state.location,
                hasLocationPermission = hasLocationPermission,
                onLocationChange = actions::onLocationChange,
                onMapInteractionChange = { isMapInteracting = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.purchase_items),
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(
                onClick = actions::onAddItem,
                modifier = Modifier.testTag(TestTagAddAddItem)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = accentColor,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = barColor
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.add_item), color = accentColor)
                }
            }
        }

        Surface(
            color = barColor,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, barButton),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.items.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_items),
                        color = textColor
                    )
                } else {
                    state.items.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = item.name,
                                onValueChange = { actions.onItemNameChange(index, it) },
                                label = { Text(stringResource(R.string.item_name)) },
                                modifier = Modifier
                                    .weight(1.6f)
                                    .testTag(TestTagAddItemNamePrefix + index),
                                colors = textFieldColors(),
                                singleLine = true,
                                maxLines = 1
                            )
                            OutlinedTextField(
                                value = item.quantity,
                                onValueChange = { actions.onItemQuantityChange(index, it) },
                                label = { Text(stringResource(R.string.item_quantity)) },
                                modifier = Modifier
                                    .weight(0.8f)
                                    .testTag(TestTagAddItemQuantityPrefix + index),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = textFieldColors(),
                                singleLine = true,
                                maxLines = 1
                            )
                            OutlinedTextField(
                                value = item.price,
                                onValueChange = { actions.onItemPriceChange(index, it) },
                                label = { Text(stringResource(R.string.item_price)) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag(TestTagAddItemPricePrefix + index),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                colors = textFieldColors(),
                                singleLine = true,
                                maxLines = 1
                            )
                            IconButton(
                                onClick = { actions.onRemoveItem(index) },
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .size(28.dp),
                                colors = IconButtonDefaults.iconButtonColors(contentColor = accentColor)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.remove_item),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (!areItemsValid(state.items)) {
            Text(
                text = stringResource(R.string.error_items_required),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = actions::onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag(TestTagAddSaveButton),
            enabled = !state.isSaving,
            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
        ) {
            Text(text = stringResource(R.string.save_receipt), color = barColor)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun isDefaultLocation(location: LatLng): Boolean {
    val defaultLat = 49.2098028
    val defaultLng = 16.6145219
    return kotlin.math.abs(location.latitude - defaultLat) < 0.0001 &&
        kotlin.math.abs(location.longitude - defaultLng) < 0.0001
}
