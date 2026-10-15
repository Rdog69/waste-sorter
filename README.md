# Waste Sorter

An Android app that tells you which bin a piece of waste belongs in. Point the camera at an item (or pick a photo from your gallery) and an on-device machine learning model classifies it as **Recyclable**, **Compost** or **Landfill**, with a confidence score and disposal instructions.

Everything runs offline on the phone: no account, no server and no images leave the device.

<!-- TODO: add screenshots or a demo GIF here, e.g. ![Identify screen](docs/identify.png) -->

## Features

- **Identify:** Take a photo with the in-app camera (CameraX) or choose one from the gallery. A MobileNetV2 model running on LiteRT (TensorFlow Lite) classifies the cropped frame and shows the result in a bottom sheet.
- **Guides:** A guide for each category with examples, what to watch out for, and which bin to use.
- **Search:** Look up over 100 common items by name or keyword. The list is loaded from `app/src/main/assets/waste_items.csv` into a local Room database.

## Tech stack

- Kotlin, MVVM (ViewModel + LiveData), View Binding
- CameraX for capture
- LiteRT (TensorFlow Lite) for on-device inference
- Room + OpenCSV for the searchable item database
- Jetpack Navigation with a bottom navigation bar
- Material Components, Glide

## Building and running

Requirements: Android Studio (recent stable), JDK 11 or newer, and an Android device or emulator running Android 7.0 (API 24) or higher.

```bash
git clone https://github.com/Rdog69/waste-sorter.git
cd waste-sorter
./gradlew installDebug   # with a device or emulator connected
```

Or open the project in Android Studio and press **Run**.

The native ML libraries are built for `armeabi-v7a` and `arm64-v8a` only, so on an emulator use an ARM system image (or an x86 image with ARM translation), or test on a physical phone.

## How the model works

`app/src/main/assets/ml/mobilenetv2_trash_classifier.tflite` is a MobileNetV2 image classifier fine-tuned for three classes: `COMPOST`, `LANDFILL` and `RECYCLABLE`. Images are resized to 224×224 and normalized to [-1, 1] before inference (see `ClassifierUtils.kt`).

<!-- TODO: name the dataset the model was trained on and link to its license, e.g.
"Trained on the TrashNet dataset (https://github.com/garythung/trashnet)". -->

## Project structure

```
app/src/main/java/com/random/example/wastesorterapp/
├── data/        Room database, DAO, repositories
├── model/       WasteItem and WasteClassification
├── ui/          Camera, result, guide and search screens
└── utils/       Classifier and image helpers
```

## Credits

- [Inter](https://rsms.me/inter/) by Rasmus Andersson, licensed under the SIL Open Font License 1.1
- [Krona One](https://fonts.google.com/specimen/Krona+One) by Yvonne Schüttler, licensed under the SIL Open Font License 1.1
- [MobileNetV2](https://arxiv.org/abs/1801.04381) architecture by Sandler et al. (Google)

## License

[MIT](LICENSE)
