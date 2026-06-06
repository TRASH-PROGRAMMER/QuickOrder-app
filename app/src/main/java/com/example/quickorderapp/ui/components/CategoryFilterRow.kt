package com.example.quickorderapp.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.quickorderapp.R

/**
 * Componente reutilizable para mostrar una fila de filtros de categorías.
 */
@Composable
fun CategoryFilterRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        categories.forEachIndexed { index, category ->
            val isSelected = category == selectedCategory
            
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colorResource(id = R.color.esmeralda),
                    containerColor = when (index % 3) {
                        0 -> colorResource(id = R.color.chip_promotion)
                        1 -> colorResource(id = R.color.azul_pastel)
                        else -> colorResource(id = R.color.chip_promotion_text)
                    }
                )
            )
            if (index < categories.size - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}
