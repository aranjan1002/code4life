Print (
        PP ('program',
	    'p05.t',
	     nil aug 'dclns',
             ('block',
                 ('if',
                    ('>','read',2),
                    ('output','true'),
                    ('output','false')
                 )
             ),
	    'p05.t'

	   ) (nil aug 3)
      )
// output: (true)
