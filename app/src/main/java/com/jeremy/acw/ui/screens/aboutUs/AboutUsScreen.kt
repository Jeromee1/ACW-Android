package com.jeremy.acw.ui.screens.aboutUs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutUsScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "ABOUT US",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "ACW stands for A Cinema Website. It was originally a web app I developed during my first semester as my final project. Three semesters later, we were told to come up with a proposal for our final project for the Mobile Online Database semester. As usual, I couldn’t think of anything I wanted to make.\n" +
                    "\n" +
                    "Then one of my classmates (Closet Femboy) said they were going to remake their previous final project. That got me thinking—why don’t I do the same? Why not just remake my first big project? This time, I had a full year of coding experience instead of four months. What could possibly go wrong?\n" +
                    "\n" +
                    "Fast forward to today. I’ve been coding non-stop. I’ve probably put well over a hundred hours into this. It just keeps going. Why do I do this to myself? I could’ve picked a simple app. Instead, the more features I add, the slower my laptop gets. My laptop can’t handle this. I can’t handle this. My applications freeze every few seconds. I’m losing my mind.\n" +
                    "\n" +
                    "Here is the intro to Star Wars: Revenge of the Sith."
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Episode III\n" +
                    "\n" +
                    "REVENGE OF THE SITH\n" +
                    "\n" +
                    "War! The Republic is crumbling\n" +
                    "under attacks by the ruthless\n" +
                    "Sith Lord, Count Dooku.\n" +
                    "There are heroes on both sides.\n" +
                    "Evil is everywhere.\n" +
                    "\n" +
                    "In a stunning move, the\n" +
                    "fiendish droid leader, General\n" +
                    "Grievous, has swept into the\n" +
                    "Republic capital and kidnapped\n" +
                    "Chancellor Palpatine, leader of\n" +
                    "the Galactic Senate.\n" +
                    "\n" +
                    "As the Separatist Droid Army\n" +
                    "attempts to flee the besieged\n" +
                    "capital with their valuable\n" +
                    "hostage, two Jedi Knights lead a\n" +
                    "desperate mission to rescue the\n" +
                    "captive Chancellor….",
            color = Color(255, 255, 0),
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(100.dp))
    }
}