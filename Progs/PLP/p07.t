Print (
        PP ('program',
	    'p07.t',
	     ('dclns',
		 ('dcln','x','integer')
             ),
             ('block',
                ('assign', 'x',3),
                ('assign', 'x',4),
                ('output', 'x')
             ),
	    'p07.t'

	   ) (nil aug 3)
      )
// output: (4)
