Print (
        PP ('program',
                'p21.t',
                        ('dclns', ('dcln', 'x', 'integer'), 
                                  ('dcln', 'y', 'integer')),
                        ('block',
                                ('assign', 'x', 0),
                                ('assign', 'y', 5),
                                ('do', 
                                        ('block',
                                                ('output', 'x', 'y'),
                                                ('assign', 'x', ('+', 'x', 1)),
                                                ('assign', 'y', ('+', 'x', 'x'))),
                                                ('<>', 'x', 3))),
                'p21.t'
           ) (nil)
      )
// output: (0, 5, 1, 2, 2, 4)
