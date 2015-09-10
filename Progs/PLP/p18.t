// Error in assignment to loop control variable.  Type mismatch.
Print (
        PP ('program',
	        'p18.t',
                ('dclns',('dcln','x','boolean')),
		('for',('assign', 'x',1),('<=', 'x', 5),
			('assign', 'x', ('+', 'x', 1)),	('output','x')),
		'p18.t'
	   ) nil 
      )
// output: error

