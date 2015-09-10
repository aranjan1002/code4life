// Error in declaration. Wrong type.
Print (
        PP ('program',
	        'p20.t',
                ('dclns',('dcln','x','color')),
		('for',('assign', 'x',1),('<=', 'x', 5),
			('assign', 'x', ('+', 'x', 1)),	('output','x')),
		'p20.t'
	   ) nil
     )
// output: error
