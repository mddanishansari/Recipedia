import 'package:flutter/material.dart';
import 'package:recipedia/ui/widget/cover.dart';

class CollapsingToolbar extends StatelessWidget {
  final int tag;
  final String title;
  final String banner;

  const CollapsingToolbar({
    required this.title,
    required this.banner,
    required this.tag,
  });

  @override
  Widget build(BuildContext context) {
    return SliverAppBar(
      floating: false,
      pinned: true,
      flexibleSpace: FlexibleSpaceBar(
        collapseMode: CollapseMode.pin,
        title: Text(title),
        background: Hero(
          tag: tag,
          child: Cover(cover: banner),
        ),
      ),
      expandedHeight: 200,
    );
  }
}
