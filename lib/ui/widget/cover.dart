import 'package:flutter/material.dart';

class Cover extends StatelessWidget {
  final String cover;

  const Cover({required this.cover});

  @override
  Widget build(BuildContext context) {
    return FadeInImage.assetNetwork(
      fadeInDuration: const Duration(milliseconds: 300),
      placeholder: 'assets/placeholder.png',
      imageErrorBuilder: (context, error, stackTrace) =>
          Image.asset('assets/placeholder.png'),
      image: cover,
      fit: BoxFit.cover,
    );
  }
}
