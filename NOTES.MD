# TODO
Exeption handling tests
Remove MerchantStrategyException

# Profiles

# Commands

## AWS
##### Deploy eb
`eb deploy bxbot-dev --staged`
##### Enable cloud watch 
`eb logs -cw enable`

# Code
## Save exchange response for mocking
`FileOutputStream f = new FileOutputStream(new File("e:\\orderBook.ser"));
 ObjectOutputStream o = new ObjectOutputStream(f);
 o.writeObject(orderBook);
 o.close();
 f.close();`

# Others
### Git repos
##### https://github.com/timmolter/XChange
XChange is a Java library providing a streamlined API 
for interacting with 60+ Bitcoin and Altcoin exchanges providing a consistent interface 
for trading and accessing market data.
##### https://github.com/ta4j/ta4j
Ta4j is an open source Java library for technical analysis

### Articles
##### Mocking
https://monkeyisland.pl/2008/04/26/asking-and-telling/
https://dzone.com/articles/a-guide-to-mocking-with-mockito