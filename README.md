# Bedwars Chat Stats

mod that sends information about your hypixel bedwars enemies to your party chat

## Roadmap / to-do list
- allow caps in aliases
- when "/bwcs aaaa" is run nothing is shown - it should probably show /bwcs help output
- click to copy in /bwcs getapikey
- order team messages and say who to target
- brain function to check if theyre nicked (if 0.00 stats then /w name) and warn if they are
- move my toggled and null key check to somewhere else or further out so that it doesnt even have to check all incoming chat msgs if mod is toggled off
- check if api key is invalid everywhere possible and if it is then show a msg about it
- i might want to find a new method for checking when game starts, lowcraw doesnt work bc i cant tell when it starts. i could use scoreboard. not urgent just a thought to make it more technical and not need to read chat messages
- make it so if youre in solos (locraw check each game) it just shows client side msgs of the same info not party chat msgs
- mod update checker based off github releases
- make everything catch errors and display the errors to the user
- i think maybe if one person is nicked it wont show any stats for the entire team
- command to check stats of a username like /stats name (like bridge duels stats mod)
  - maybe add duels & specific duels types support too
- command to get info about party members (/pl and intercept the msg to not show it to the player)
- check if you are nicked if it will remove your team or not from the teams list
- it'll randomly sometimes not say anything
- warning for players over a settable fkdr
- be able to set ws warning