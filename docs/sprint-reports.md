# Sprint Report




## Git Report

To get stat from git for current sprint - in this example sprint 7 - simply use
previous sprint tag as your starting point.

### Commiters

```
g log sprint-6..HEAD --format="%an" | sort | uniq -c
```


### Change Log

```
g log sprint-6..HEAD --format="%<(72)%s %<(16)%an" \
  | sed 's/\[\([^ ]*\)\] \(.*\)/[\1] \2 http:\/\/jira.digital.nhs.uk\/browse\/\1/'
```

```
g log sprint-6..HEAD --shortstat --oneline \
  | grep -E "^ " \
  | grep "deletion" \
  | sed 's/.* \([0-9]*\) delet.*/\1/' \
  | awk '{s+=$1} END {print s}'
```

```
g log sprint-6..HEAD --shortstat --oneline \
  | grep -E "^ " \
  | grep "insert" \
  | sed 's/.* \([0-9]*\) insert.*/\1/' \
  | awk '{s+=$1} END {print s}'
```
