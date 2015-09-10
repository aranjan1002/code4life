Print(
        // Error in declaration. Variable redeclared.
        PP ('program',
	        'p19.t',
                ('dclns',('dcln','x','boolean'),
                         ('dcln','x','integer')),
		('for',('assign', 'x',1),('<=', 'x', 5),
			('assign', 'x', ('+', 'x', 1)),	('output','x')),
		'p19.t'
	   ) nil
     )

// output: error
