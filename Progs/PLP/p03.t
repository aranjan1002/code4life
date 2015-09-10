Print (
        PP ('program',
	    'p03.t',
	     nil aug 'dclns',
             ('block',
                  ('output',
		     ('<=','read',2),
		     ('=', 'read',2),
		     ('>=','read',2),
		     ('<>','read',2),
		     ('<', 'read',2),
		     ('>', 'read',2)
                  )
             ),
	    'p03.t'

	   ) (3,3,3,3,3,3)
      )
// output: (false, false, true, true, false, true)
