function  cplexlpex
% Use the function cplexlp to solve a linear programming problem
%
% The LP problem solved in this example is
%   Maximize  x1 + 2 x2 + 3 x3
%   Subject to
%      - x1 +   x2 + x3 <= 20
%        x1 - 3 x2 + x3 <= 30
%   Bounds
%        0 <= x1 <= 40
%        0 <= x2
%        0 <= x3

% ---------------------------------------------------------------------------
% File: cplexlpex.m
% Version 12.2
% ---------------------------------------------------------------------------
% Licensed Materials - Property of IBM
% 5725-A06 5725-A29 5724-Y48 5724-Y49 5724-Y54 5724-Y55
% Copyright IBM Corporation 2008, 2010. All Rights Reserved.
%
% US Government Users Restricted Rights - Use, duplication or
% disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
% ---------------------------------------------------------------------------

try
   % Since cplexlp solves minimization problems and the problem
   % is a maximization problem, negate the objective
   f     = [-1 -2 -3]';
   Aineq = [-1  1  1;  1 -3  1];
   bineq = [20 30]';
   
   lb = [0    0   0]';
   ub = [40 inf inf]';
   
   options = cplexoptimset;
   options.Diagnostics = 'on';
   
   [x, fval, exitflag, output] = cplexlp ...
      (f, Aineq, bineq, [], [], lb, ub, [], options);
   
   fprintf ('\nSolution status = %s\n', output.cplexstatusstring);
   fprintf ('Solution value = %f\n', fval);
   disp ('Values =');
   disp (x');
catch m
   disp(m.message);
end
end
