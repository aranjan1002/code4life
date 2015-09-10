Print (
        PP ('program',
	    'p24.t',
	     ('dclns',
		 ('dcln','x','y','integer')
             ),
             ('block',
		('for',
		   ('block',
		       ('assign','x',1),
		       ('assign','y',15)
		   ),
		   ('<=','x', 2),
		   ('block',
		       ('assign','x',('+','x',1)),
		       ('assign','y',('-','y',1))
		   ),
		   ('output', ('*','x','x'),'y')
                )
             ),
	    'p24.t'

	   ) (2,3,4,5)
      )

//  output: (1, 15, 4, 14)
