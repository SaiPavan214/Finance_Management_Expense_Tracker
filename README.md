💰 Finance Management & Expense Tracker

A mobile app built with Kotlin & Jetpack Compose that helps users securely track their income and expenses, store data offline, and sync with Firebase for authentication.

✨ Features

✅ Secure Authentication with Firebase

✅ Offline Storage with Room DB

✅ Track Income & Expenses with category support

✅ Dynamic Reports with charts & analytics

✅ MVVM Architecture for clean, scalable development

✅ Modern UI with Jetpack Compose (Material Design 3)

🚀 Tech Stack
Technology	Role
Kotlin	Core programming language
Jetpack Compose	Declarative UI toolkit
Room DB	Offline database for expenses
Firebase Auth	Secure login & authentication
MVVM	Architecture for separation of concerns
Material Design	Modern responsive UI/UX
📁 Project Structure
Finance-Management-Expense-Tracker/
│
├── app/                  # Main Android application
│   ├── data/             # Room DB entities, DAOs, repositories
│   ├── ui/               # Jetpack Compose UI screens
│   ├── viewmodel/        # ViewModels (MVVM architecture)
│   ├── auth/             # Firebase authentication logic
│   └── utils/            # Helper functions, constants
│
├── screenshots/          # App screenshots for README
├── build.gradle          # Project dependencies
├── settings.gradle
└── README.md

⚙️ Setup Instructions
1. Clone the repository
git clone https://github.com/SaiPavan214/Finance-Management-Expense-Tracker.git
cd Finance-Management-Expense-Tracker

2. Open in Android Studio

Open the project in Android Studio (Arctic Fox or later)

Make sure you have Android SDK 21+ installed

3. Configure Firebase

Go to Firebase Console

Create a new project & enable Authentication (Email/Password)

Download the google-services.json file and place it in:

app/google-services.json

4. Build & Run

Connect an Android device / start an emulator

Run the app via Android Studio → Run ▶

📌 Future Improvements

📊 Advanced analytics & insights with AI

🌐 Cloud Firestore sync for cross-device access

🔔 Budget alerts & notifications

📱 Export reports to PDF/Excel

🌙 Dark Mode support

🛡 Security

Firebase handles authentication securely

Room DB ensures offline-first storage

API keys & configs stored outside version control

🤝 Contributions

Pull requests are welcome!
If you find bugs or have ideas for improvements, feel free to open an issue or PR.

📄 License

This project is licensed under the MIT License.

👨‍💻 Author

Made with ❤️ by Sai Pavan
🔗 GitHub: SaiPavan214
