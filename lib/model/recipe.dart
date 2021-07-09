class Recipe {
  final String bannerPath;
  final String cookingTime;
  final int id;
  final List<String> ingredients;
  final String makes;
  final String name;
  final String preparationTime;
  final List<String> steps;
  final String serves;

  Recipe({
    required this.bannerPath,
    required this.cookingTime,
    required this.id,
    required this.ingredients,
    required this.makes,
    required this.name,
    required this.preparationTime,
    required this.steps,
    required this.serves,
  });

  String get banner {
    return 'https://raw.githubusercontent.com/mddanishansari/Recipedia/master/recipe_banners' +
        bannerPath;
  }

  factory Recipe.fromJson(Map<String, dynamic> json) {
    return Recipe(
      bannerPath: json['banner_path'],
      cookingTime: json['cooking'],
      id: json['id'],
      ingredients: List<String>.from(json['ingredients']),
      makes: json['makes'],
      name: json['name'],
      preparationTime: json['preparation'],
      steps: List<String>.from(json['steps']),
      serves: json['serves'],
    );
  }
}
