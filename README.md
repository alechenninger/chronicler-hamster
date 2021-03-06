# chronicler-hamster
[Hamster](https://github.com/projecthamster/hamster) plugin for [chronicler](https://github.com/alechenninger/chronicler)

## Using

Assuming a source plugin path to jar is defined in your chronicler config...

```bash
chronicler -hc categories.json -hr report.xml
```

Where categories.json is a file in this format:

```json
{
  "Hamster category name": {
    "project": "Corresponding project in rally timesheets",
    "workProduct": "Corresponding work product",
    "task": "Corresponding task (optional)"
  }
}
```

And report.xml is an xml time report generated from hamster like...

```bash
hamster export xml 2015-04-01 2015-04-18 > report.xml
```

Or if you have time entries already, you can auto generate a report from the date of the last time entry until today:

```bash
chronicler -hc categories.json -ha
```

You can add `["-hc", "categories.json", "-ha"]` to sourcePlugin.args in chronicler config.json. Then you can just run "chronicler" with no args.
