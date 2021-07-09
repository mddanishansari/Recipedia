import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:recipedia/model/category.dart';
import 'package:recipedia/ui/screen/recipe_list_screen.dart';
import 'package:recipedia/ui/widget/list_item.dart';

class CategoryItem extends StatelessWidget {
  final Category category;

  const CategoryItem({required this.category});

  @override
  Widget build(BuildContext context) {
    return ListItem(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => RecipeListScreen(category: category),
          ),
        );
      },
      banner: category.banner,
      title: category.name,
      tag: category.id,
    );
  }
}
