Print (
        PP ('program',
	    'p08.t',
	     nil aug 'dclns',
             ('block',
		('while',
		   ('not','eof'),
		   ('output','read')
                )
             ),
	    'p08.t'

	   ) (3,1,2)
      )
// output: (3, 1, 2)
