import 'package:flutter/material.dart';
import 'package:recipedia/model/recipe.dart';
import 'package:recipedia/ui/widget/collapsing_toolbar.dart';
import 'package:recipedia/ui/widget/recipe_bullet_list_items.dart';
import 'package:recipedia/ui/widget/recipe_list_items.dart';

class RecipeScreen extends StatelessWidget {
  final Recipe recipe;

  const RecipeScreen({required this.recipe});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: true,
      body: CustomScrollView(
        slivers: [
          CollapsingToolbar(
            title: recipe.name,
            banner: recipe.banner,
            tag: recipe.id,
          ),
          SliverList(
            delegate: SliverChildListDelegate([
              Column(
                children: [
                  Makes(body: recipe.makes),
                  Preparation(body: recipe.preparationTime),
                  Cooking(body: recipe.cookingTime),
                  Serves(body: recipe.serves),
                  Ingredients(items: recipe.ingredients),
                  Steps(items: recipe.steps),
                ],
              )
            ]),
          ),
        ],
      ),
    );
  }
}
