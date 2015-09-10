Print (
        PP ('program',
	    'p22.t',
	     nil aug 'dclns',
             ('block',
                  ('output',
		     ('or', 'false','false'),
		     ('or', 'false','true'),
		     ('or', 'true', 'false'),
		     ('or', 'true', 'true'),
		     ('and', 'false','false'),
		     ('and', 'false','true'),
		     ('and', 'true', 'false'),
		     ('and', 'true', 'true'),
		     ('not','true'),
		     ('not',('not','true'))
                  )
             ),
	    'p22.t'

	   ) nil
      )
// output: (false, true, true, true, false, false, false, true, false, true)
