import 'package:flutter/material.dart';
import 'package:recipedia/model/category.dart';
import 'package:recipedia/ui/widget/recipe_item.dart';

class RecipeListScreen extends StatelessWidget {
  final Category category;

  const RecipeListScreen({required this.category});

  @override
  Widget build(BuildContext context) {
    final recipes = category.recipes;
    return Scaffold(
      appBar: AppBar(
        title: Text(category.name),
      ),
      body: ListView.builder(
          itemCount: category.recipes.length,
          itemBuilder: (context, index) {
            final recipe = recipes[index];
            return RecipeItem(recipe: recipe);
          }),
    );
  }
}
