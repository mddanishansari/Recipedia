import 'package:flutter/material.dart';

class BulletList extends StatelessWidget {
  final String title;
  final List<String> items;

  BulletList({required this.title, required this.items});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(2.0),
      child: Card(
        child: ListTile(
          title: Text(title),
          subtitle: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: items.map((str) {
              return Padding(
                padding: const EdgeInsets.symmetric(vertical: 4),
                child: Row(
                  children: [
                    Text(
                      '\u2022',
                    ),
                    SizedBox(
                      width: 6,
                    ),
                    Expanded(
                      child: Container(
                        child: Text(
                          str,
                          softWrap: true,
                        ),
                      ),
                    ),
                  ],
                ),
              );
            }).toList(),
          ),
        ),
      ),
    );
  }
}

class Ingredients extends StatelessWidget {
  final List<String> items;

  const Ingredients({required this.items});

  @override
  Widget build(BuildContext context) {
    return BulletList(
      title: 'Ingredients',
      items: items,
    );
  }
}

class Steps extends StatelessWidget {
  final List<String> items;

  const Steps({required this.items});

  @override
  Widget build(BuildContext context) {
    return BulletList(
      title: 'Steps',
      items: items,
    );
  }
}
