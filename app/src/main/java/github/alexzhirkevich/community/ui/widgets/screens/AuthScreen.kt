package github.alexzhirkevich.community.ui.widgets.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import github.alexzhirkevich.community.R
import github.alexzhirkevich.community.ui.theme.Colors
import github.alexzhirkevich.community.ui.theme.Dimens
import github.alexzhirkevich.community.ui.theme.ifInDarkMode

@Composable
fun AuthScreen(navController: NavHostController) {

    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(170.dp),
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo"
            )
            Column(
                modifier = Modifier.width(300.dp)
                    .height(IntrinsicSize.Min)
            ) {
                TextField(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Colors.Gray,
                            shape = CircleShape
                        )
                        .fillMaxWidth()
                        .background(color = Color.Transparent),
                    value = phoneNumber,
                    singleLine = true,
                    shape = CircleShape,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    onValueChange ={phoneNumber = it},
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
//                    visualTransformation = CameroonNumberVisualTransformation()
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = ifInDarkMode(Colors.White, Colors.Black),
                            shape = CircleShape
                        )
                        .clip(CircleShape),
                    onClick = {
                              navController.navigate(Routes.BottomNavigationScreen){
                                  popUpTo(navController.graph.startDestinationId){
                                      inclusive = true
                                  }
                                  launchSingleTop = true
                              }
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.send_code),
                        color = ifInDarkMode(Colors.Black, Colors.White),
                        fontSize = Dimens.FontSizeBig,
                        fontWeight = FontWeight.Bold,

                    )
                }
            }
        }
    }
}

class CameroonNumberVisualTransformation: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Make the string XXX-XXX-XXX
        val trimmed = if (text.text.length >= 9) text.text.substring(0..8) else text.text
        var output = ""
        for (i in trimmed.indices) {
            output += trimmed[i]
            if (i% 3 == 2 && i != 8) output +="-"
        }


        /**
         * The offset works such that the translator ignores hyphens. Conversions
         * from original to transformed text works like this
        - 3rd character in the original text is the 4th in the transformed text
        - The 6th character in the original becomes the 8th
        In reverse, the conversion is such that
        - 10th Character in transformed becomes the 8th in original
        - 4th in transformed becomes 3rd in original
         */

        val cameroonNumberTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // [offset [0 - 2] remain the same]
                if (offset <= 2) return offset
                // [3 - 5] transformed to [4 - 6] respectively
                if (offset <= 5) return offset + 1
                // [6 - 8] transformed to [8 - 10] respectively
                if (offset <= 8 ) return offset + 2
                return 11
            }

            override fun transformedToOriginal(offset: Int): Int {

                if (offset <= 2) return offset
                if (offset <= 6) return offset -1
                if (offset <= 10) return offset - 2
                return 9

            }

        }

        return TransformedText(
            AnnotatedString(output),
            cameroonNumberTranslator)

    }

}