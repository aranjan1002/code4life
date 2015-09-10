 // Test Different things.
 Print (
	PP ('program',
	        'p13.t',
                ('dclns',('dcln','x','y','integer'),
                         ('dcln','a','b','boolean')),
		('block',('assign','x','read'),
	                 ('assign','y','read'),
                         ('if',('=','x',3),
                               ('assign','a','true'),
                               ('assign','b','false')),
			 ('output','a'),
			 ('while',('<=','x','y'),
				('block',('output','x','y'),
					 ('assign','x',('+','x',1))))),
		'p13.t'
	   ) (nil aug 3 aug 3)
       )
// output: (true, 3, 3)
