import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart' show rootBundle;
import 'package:recipedia/model/category.dart';
import 'package:recipedia/ui/widget/category_item.dart';
import 'package:shared_preferences/shared_preferences.dart';

class HomeScreen extends StatefulWidget {
  final ValueSetter<ThemeMode> onThemeChanged;

  const HomeScreen({required this.onThemeChanged});

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  IconData? _themeModeIcon;

  late Future<List<Category>> _recipeCategoriesFuture;

  Future<List<Category>> _loadRecipeCategories() async {
    var jsonText = await rootBundle.loadString('assets/recipe_api.json');
    return List<Category>.from(
      jsonDecode(jsonText).map(
        (jsonObject) => Category.fromJson(jsonObject),
      ),
    );
  }

  @override
  void initState() {
    _setInitialThemeIcon();
    super.initState();
    _recipeCategoriesFuture = _loadRecipeCategories();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Recipedia'),
        actions: [
          IconButton(
            onPressed: () {
              _changeTheme();
            },
            icon: Icon(_themeModeIcon),
          )
        ],
      ),
      body: _buildBody(),
    );
  }

  void _setInitialThemeIcon() async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    int initialTheme = (preferences.getInt('theme') ?? ThemeMode.system.index);
    setState(() {
      _themeModeIcon = _getThemeIcon(initialTheme);
    });
  }
  
  // system -> light -> dark -> system
  void _changeTheme() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    int theme = (prefs.getInt('theme') ?? ThemeMode.system.index);
    int nextTheme = theme + 1;
    if (nextTheme > 2) {
      nextTheme = ThemeMode.system.index;
    }

    // set value in shared preference and invoke the callback
    await prefs.setInt('theme', nextTheme);
    widget.onThemeChanged(ThemeMode.values[nextTheme]);

    // update the icon
    setState(() {
      _themeModeIcon = _getThemeIcon(nextTheme);
    });
  }

  IconData _getThemeIcon(int index) {
    IconData _themeModeIcon = Icons.brightness_auto_outlined;
    switch (index) {
      case 0:
        _themeModeIcon = Icons.brightness_auto_outlined;
        break;
      case 1:
        _themeModeIcon = Icons.light_mode_outlined;
        break;
      case 2:
        _themeModeIcon = Icons.dark_mode_outlined;
        break;
    }
    return _themeModeIcon;
  }

  FutureBuilder<List<Category>> _buildBody() {
    return FutureBuilder<List<Category>>(
      future: _recipeCategoriesFuture,
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          final recipeCategories = snapshot.data!;
          return ListView.builder(
              padding: EdgeInsets.all(4),
              itemCount: recipeCategories.length,
              itemBuilder: (context, index) {
                final category = recipeCategories[index];
                return CategoryItem(category: category);
              });
        }

        if (snapshot.hasError) {
          return Center(
            child: Text(snapshot.error.toString()),
          );
        }
        return Center(
          child: CircularProgressIndicator(),
        );
      },
    );
  }
}
