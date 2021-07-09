import 'package:flutter/material.dart';

class RecipeListItem extends StatelessWidget {
  final String title;
  final String body;

  const RecipeListItem({required this.title, required this.body});

  @override
  Widget build(BuildContext context) {
    return body.isNotEmpty
        ? Padding(
            padding: const EdgeInsets.all(2.0),
            child: Card(
              child: ListTile(
                title: Text(title),
                subtitle: Text(body),
              ),
            ),
          )
        : Container();
  }
}

class Makes extends StatelessWidget {
  final String body;

  const Makes({required this.body});

  @override
  Widget build(BuildContext context) {
    return RecipeListItem(title: 'Makes', body: body);
  }
}

class Preparation extends StatelessWidget {
  final String body;

  const Preparation({required this.body});

  @override
  Widget build(BuildContext context) {
    return RecipeListItem(title: 'Preparation', body: body);
  }
}

class Cooking extends StatelessWidget {
  final String body;

  const Cooking({required this.body});

  @override
  Widget build(BuildContext context) {
    return RecipeListItem(title: 'Cooking', body: body);
  }
}

class Serves extends StatelessWidget {
  final String body;

  const Serves({required this.body});

  @override
  Widget build(BuildContext context) {
    return RecipeListItem(title: 'Serves', body: body);
  }
}
