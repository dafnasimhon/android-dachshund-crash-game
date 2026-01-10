**🐶 Sausage Dog Game**

An Arcade Adventure for Android

**📖 Overview**

Sausage Dog Game is a fast-paced arcade experience where you guide a Dachshund through a world of falling obstacles and treats. Navigate across 5 lanes using either traditional buttons or immersive motion sensors, while tracking your global high scores via GPS integration.

**🎮 About the Game**

The player navigates the dog across 5 different lanes. Items fall from the top of the screen at varying speeds:

Hotdogs🌭 (Obstacles): Colliding with these results in the loss of a life.

Dog Food🦴 (Bonuses): Collecting these rewards the player by increasing the odometer distance by +10.

Game Over❤️: The session ends once the player loses all three hearts (lives).

**🛠 Key Features**

🕹 Dual Control Modes
Players can choose their preferred control scheme from the main menu:

Buttons Mode: Standard control using Left/Right buttons on the screen. In this mode, players can also pre-select the starting game speed.

Sensors Mode:

Movement: Tilting the phone left or right moves the dog between lanes.

Fast Mode (Turbo): Tilting the phone forward accelerates the game speed. Tilting it backward returns the game to normal speed.

Note: Buttons are hidden in this mode for a cleaner, more immersive experience.

**📍 Location Services & High Scores**

GPS Integration: Upon game over, the app uses FusedLocationProviderClient to capture the player's geographic coordinates (Latitude and Longitude) along with their final score.

Score Management: Records are handled by a RecordListManager and displayed in a dedicated High Score table.

Map Integration: The high score screen features a map that highlights the specific location where each high score was achieved.

**🔊 User Experience (UX)**

Sound Effects: Powered by SingleSoundPlayer to trigger audio cues for collisions ("Oops") and bonuses ("Yummy").

Haptics (Vibration): Physical feedback is triggered via SignalManager every time the dog hits an obstacle.

Digital Odometer: A real-time digital display tracks the distance the player has successfully traveled.

**🏗 System Architecture (Main Components)**

MainActivity.kt
The central hub managing the "Game Loop":

gameTick(): Triggered by the GameTimer to update game logic and refresh the UI.

drawBoard(): Dynamically updates the 5x5 ImageView matrix based on the current board state provided by the GameManager.

updateSpeed(): A dynamic function that adjusts the timer delay in real-time during Sensor Mode.

TiltDetector.kt
Handles the processing of Accelerometer data:

Separates input between the X-axis (side-to-side movement) and Y-axis (speed/tilt forward).

Uses a Callback interface to communicate detected movements back to the Activity.

GameManager.kt
The core engine of the game:

Manages the dog's position, collision detection, life count, and distance calculation.

**📱 Visual Overview**

icon:

<img width="114" height="136" alt="image" src="https://github.com/user-attachments/assets/9be894f8-1d32-41ef-a54f-57965d50517d" />


Menu: Speed selection and control mode toggle.

<img width="404" height="790" alt="image" src="https://github.com/user-attachments/assets/56dd03e3-200a-483f-972f-fa0ed7ff1022" />


Game Screen: 5x5 grid display, heart/life indicators, and the digital odometer.

<img width="527" height="816" alt="image" src="https://github.com/user-attachments/assets/f428224c-9cc8-428f-b741-96f2e80f239b" />


Score Screen: A table with rounded corners and an integrated Google Map for score locations.

<img width="441" height="795" alt="image" src="https://github.com/user-attachments/assets/54e4f364-a981-49a7-a0de-e57a6026b44d" />

