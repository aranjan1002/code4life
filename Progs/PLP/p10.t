Print (
        PP ('program',
	    'p10.t',
	     nil aug 'dclns',
             ('block',
		('do',
		   ('output',3),
		   ('<=','read',3)
                )
             ),
	    'p10.t'

	   ) (2,3,4,5)
      )
// output: (3, 3, 3)
