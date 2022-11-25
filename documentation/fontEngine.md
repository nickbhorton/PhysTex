<script
  src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"
  type="text/javascript">
</script>
# Putting Together The Symbols
**Contours:** Each symbol is made out of a number of contours. A contour is a closed shape that has a parameterization direction. Each contour is defines by a set of points $(n \geq 3)$. The set of points are related in groups of three (quadratic bezier curve) or a pair (line / linear bezier curve). The first point of the first curve and the last point of the last curve have to be the same. It is important for contours to have a direction. The direction allows for the rasterization algorithm to work.

**Contour Space:** Each point is defined originally in contour space. Contour space is a cartesian product of the natural numbers from 0 up to 32768 $(2^{15})$. This allows for very fine tune adjustment of each curve.

**Important Symbol Quantities:**
- baseline: a y value where the font will sit on a line
- left boundary: furthest point left
- right boundary: furthest point right