import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financemanagementapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenu(
    onSettingsClick: () -> Unit,
    onDeleteResetClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAddNewCategoriesClick: () -> Unit
) {
    Column {
        Text(
            text = "Finance Management App",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )
        Divider(color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .clickable { onSettingsClick() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Settings")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .clickable { onDeleteResetClick() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Delete and Reset",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Delete")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .clickable { onHelpClick() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.question),
                contentDescription = "Help",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Help")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .clickable { onAddNewCategoriesClick() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add New Categories",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Add New Categories")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewCategoryDialog(
    onDismiss: () -> Unit,
    onSaveCategory: (String, Int) -> Unit
) {
    var categoryName by remember { mutableStateOf(TextFieldValue()) }
    var selectedIconIndex by remember { mutableStateOf(0) }
    val icons = listOf(
        R.drawable.trophy,
        R.drawable.coupons,
        R.drawable.grants,
        R.drawable.lottery,
        R.drawable.refund,
        R.drawable.rental,
        R.drawable.salary,
        R.drawable.sale
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Add New Category")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Select Icon:")
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    items(icons.size) { index ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(start = if (index == 0) 16.dp else 8.dp)
                                .clickable {
                                    selectedIconIndex = index
                                }
                                .size(56.dp)
                                .background(
                                    color = if (selectedIconIndex == index) Color.LightGray else Color.Transparent,
                                    shape = MaterialTheme.shapes.small
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = icons[index]),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveCategory(categoryName.text, icons[selectedIconIndex])
                    onDismiss()
                }
            ) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        }
    )
}

