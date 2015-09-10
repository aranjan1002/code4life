	// Test eof. No declarations!
Print (
	PP ('program',
	        'p14.t',
                nil aug 'dclns',
		('while',('not','eof'),('output','read')),
		'p14.t'
	   ) (nil aug 1 aug 2 aug 3 aug 4 aug 5)  
      )

// output: (1, 2, 3, 4, 5)
