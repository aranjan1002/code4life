Print (
        PP ('program',
	    'p23.t',
	     ('dclns',
		 ('dcln','x','integer')
             ),
             ('block',
		   ('assign', 'x','read'),
		   ('case', 'x',
		     ('c_clause',4,('assign','x',3)),
		     ('c_clause',3,('assign','x','read')),
		     ('c_clause',3,('assign','x',5))
                   ),
                   ('output', 'x')
             ),
	    'p23.t'

	   ) (3,4)
      )

// output: (4)

