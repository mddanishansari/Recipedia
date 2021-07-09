import 'package:recipedia/model/recipe.dart';

class Category {
  final int id;
  final String name;
  final String bannerPath;
  final List<Recipe> recipes;

  Category({
    required this.id,
    required this.name,
    required this.bannerPath,
    required this.recipes,
  });

  String get banner {
    return 'https://raw.githubusercontent.com/mddanishansari/Recipedia/master/recipe_banners' +
        bannerPath;
  }

  factory Category.fromJson(Map<String, dynamic> json) {
    return Category(
      id: json['id'],
      name: json['name'],
      bannerPath: json['banner_path'],
      recipes: List<Recipe>.from(
        json['recipes'].map(
          (jsonObject) => Recipe.fromJson(jsonObject),
        ),
      ),
    );
  }
}
