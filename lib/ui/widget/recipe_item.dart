import 'package:flutter/material.dart';
import 'package:recipedia/model/recipe.dart';
import 'package:recipedia/ui/screen/recipe_screen.dart';
import 'package:recipedia/ui/widget/list_item.dart';

class RecipeItem extends StatelessWidget {
  final Recipe recipe;

  const RecipeItem({required this.recipe});

  @override
  Widget build(BuildContext context) {
    return ListItem(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => RecipeScreen(
              recipe: recipe,
            ),
          ),
        );
      },
      banner: recipe.banner,
      title: recipe.name,
      tag: recipe.id,
    );
  }
}
