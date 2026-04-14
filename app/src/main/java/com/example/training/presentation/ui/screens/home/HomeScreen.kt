package com.example.training.presentation.ui.screens.home

import android.R.attr.contentDescription
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ContentDescription
import androidx.compose.ui.unit.dp
import com.example.training.R

//@Composable
//fun TopBarSection(){
//    Row(
//        modifier = Modifier.fillMaxWidth()
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Start
//    ) {
//        //Menu Button
//        IconButton(modifier = Modifier,
//            onClick = {}
//        ) {
//            androidx.compose.material3.Icon(
//                imageVector = Icons.Default.Menu,
//                contentDescription = "Top bar menu",
//                tint = Color.DarkGray
//            )
//        }
//
//        //screen title
//        Text(text = "Home", modifier = Modifier.weight(1f))
//
//        //Country flag
//        Image(
//            painter = painterResource(R.drawable.ic_post_background),
//            contentDescription = "Country flag",
//            modifier = Modifier
//                .weight(1f)
//                .size(32.dp)
//                .align(Alignment.Top)
//                .clip(RoundedCornerShape(4.dp))
//        )
//    }
//}


@Composable
fun TopBarSection() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        //Menu button
        IconButton(onClick = {}) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.DarkGray
            )
        }

        //Screen title
        Text(
            text = "Home",
            style = MaterialTheme.typography.bodyLarge
        )

        //Flag
        Image(
            painter = painterResource(R.drawable.ic_post_background),
            contentDescription = "Flag",
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
    }
}
