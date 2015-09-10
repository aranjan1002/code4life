Print (
        PP ('program',
            'p02.t',
             ('dclns',
                 ('dcln','x','integer'),
                 ('dcln','y','integer')
             ),
             ('block',
                ('assign','x','read'),
                ('case', 'x',
                   ('c_clause',1,('output', 3)),
                   ('c_clause',3,('output', 1)),
                   ('c_clause',4,('output', 3)),
                   ('c_clause',3,('output', 2))
                )
             ),
            'p02.t'

           ) (3,7)
      )
