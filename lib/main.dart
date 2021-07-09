import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:recipedia/ui/screen/home_screen.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  runApp(RecipediaApp());
}

class RecipediaApp extends StatefulWidget {
  @override
  _RecipediaAppState createState() => _RecipediaAppState();
}

class _RecipediaAppState extends State<RecipediaApp> {
  ThemeMode? _themeMode;

  @override
  void initState() {
    _setInitialTheme();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Recipedia',
      theme: ThemeData(
        fontFamily: GoogleFonts.lato().fontFamily,
        primarySwatch: Colors.deepOrange,
      ),
      darkTheme: ThemeData.dark(),
      themeMode: _themeMode,
      home: HomeScreen(
        onThemeChanged: (themeMode) {
          print(themeMode);
          setState(() {
            _themeMode = themeMode;
          });
        },
      ),
    );
  }

  void _setInitialTheme() async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    int initialTheme = (preferences.getInt('theme') ?? ThemeMode.system.index);
    setState(() {
      _themeMode = ThemeMode.values[initialTheme];
    });
  }
}
