## Contributing

We use GitHub to keep track of our issues. 

The name of the git branch should start with the task's GitHub issue number, e.g. `18-style`.

The first word describes what's happening in the commit.
- `feat` for new functionality
- `fix` for bug fix or correction of typographical errors
- `style` for fixing code style
- `docs` for writing documentation
- `refactor` for restructuring of code
- `test` for changes in tests
- `build` for things related to the build system
- `chore` for everything else

Then comes `#<task number>` in parentheses, followed by a sentence about the commit.
E.g feat(#12): adds a getter

We want to be able to merge often to avoid larger merge conflicts and end up many commits behind. Thus, there can rather exist several branches with the same number.

A thorough review of all code should be done before it is merged into main.

No direct changes should occur on main.