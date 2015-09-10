Print (
        PP ('program',
	    'p06.t',
	     nil aug 'dclns',
             ('block',
                 ('if',
                    ('>','read',2),
                    ('output','true')
                 ),
                 ('if',
                    ('<','read',2),
                    ('output','true')
                 )
             ),
	    'p06.t'

	   ) (3,4)
      )
// output: (true)
