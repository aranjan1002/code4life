	// Declare x, output loop variable.
Print (
        PP ('program',
	        'p12.t',
                ('dclns',('dcln','x','integer')),
		('for',('assign', 'x',1),('<=', 'x', 5),
			('assign', 'x', ('+', 'x', 1)),	('output','x')),
		'p12.t'
	   ) nil
      )
// output: (1, 2, 3, 4, 5)
