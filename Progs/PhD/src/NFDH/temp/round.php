<?php
/* Using PHP_ROUND_HALF_UP with 1 decimal digit precision */
echo round( 1.55, 1, PHP_ROUND_HALF_UP);   //  1.6
echo round( 1.54, 1, PHP_ROUND_HALF_UP);   //  1.5
echo round(-1.55, 1, PHP_ROUND_HALF_UP);   // -1.6
echo round(-1.54, 1, PHP_ROUND_HALF_UP);   // -1.5

/* Using PHP_ROUND_HALF_DOWN with 1 decimal digit precision */
echo round( 1.55, 1, PHP_ROUND_HALF_DOWN); //  1.5
echo round( 1.54, 1, PHP_ROUND_HALF_DOWN); //  1.5
echo round(-1.55, 1, PHP_ROUND_HALF_DOWN); // -1.5
echo round(-1.54, 1, PHP_ROUND_HALF_DOWN); // -1.5

/* Using PHP_ROUND_HALF_EVEN with 1 decimal digit precision */
echo round( 1.55, 1, PHP_ROUND_HALF_EVEN); //  1.6
echo round( 1.54, 1, PHP_ROUND_HALF_EVEN); //  1.5
echo round(-1.55, 1, PHP_ROUND_HALF_EVEN); // -1.6
echo round(-1.54, 1, PHP_ROUND_HALF_EVEN); // -1.5

/* Using PHP_ROUND_HALF_ODD with 1 decimal digit precision */
echo round( 1.55, 1, PHP_ROUND_HALF_ODD);  //  1.5
echo round( 1.54, 1, PHP_ROUND_HALF_ODD);  //  1.5
echo round(-1.55, 1, PHP_ROUND_HALF_ODD);  // -1.5
echo round(-1.54, 1, PHP_ROUND_HALF_ODD);  // -1.5
?>
