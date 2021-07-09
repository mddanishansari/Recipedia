import 'package:flutter/material.dart';
import 'package:recipedia/ui/widget/cover.dart';

class ListItem extends StatelessWidget {
  final GestureTapCallback onTap;
  final String banner;
  final String title;
  final int tag;

  const ListItem(
      {required this.onTap, required this.banner, required this.title, required this.tag});

  @override
  Widget build(BuildContext context) {
    return Card(
      child: InkWell(
        onTap: onTap,
        child: Column(
          children: [
            SizedBox(
              height: 200,
              width: double.infinity,
              child: ClipRRect(
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(4),
                  topRight: Radius.circular(4),
                ),
                child: Hero(
                  tag: tag,
                  child: Cover(
                    cover: banner,
                  ),
                ),
              ),
            ),
            ListTile(
              title: Text(title),
            )
          ],
        ),
      ),
    );
    ;
  }
}
