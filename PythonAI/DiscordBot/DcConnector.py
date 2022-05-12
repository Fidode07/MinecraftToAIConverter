import discord
from discord.ext import commands


class varManager:
    def __init__(self) -> None:
        self.create_channel_message: str = "Bitte warte ... Ich erstelle einen Sprachkanal für dich!"
        self.create_channel_succefully_message: str = "Sprachkanal Erfolgreich erstellt. Bitte suche nach dem " \
                                                      "Sprachkanal mit deinem Namen, und trete ihm bei! "


bot = commands.Bot(command_prefix='!')
vMan = varManager()


@bot.command()
async def createvoice(ctx):
    print("Create Voicechannel ...")
    try:
        print("Succefully created Channel")
        guild = ctx.guild
        member = ctx.author
        overwrites = {
            guild.me: discord.PermissionOverwrite()
        }
        channel = await guild.create_voice_channel(ctx.author, overwrites=overwrites)
    except Exception as e:
        print("FUCK!")
        print(e)

bot.run('OTc0MDUzNjA2Mjg0MTk3OTQ4.GNP-xp.ZKCptLlJ2a1zBqOisvCfrAuGwVhndQqfTovghM')
