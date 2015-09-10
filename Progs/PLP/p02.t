Print (
        PP ('program',
	    'p02.t',
	     ('dclns',
		 ('dcln','x','integer'),
		 ('dcln','y','integer')
             ),
             ('block',
		('assign','x','read'),
                ('output',
		   ('+','x',3),
		   ('-','x',3),
		   ('*','x',3),
		   ('/','x',3),
		   ('+',2,('+','read',3))
                )
             ),
	    'p02.t'

	   ) (3,7)
      )
// output: (6, 0, 9, 1, 12)
