# Project Title
Twittiment

# Introduction
Twitter is a social network and microblogging system which limits a single content to 140 words. This limitation did not hinder the grows of users but rather attract people or organization to share, advertise or interact on the platform. 50 millions tweets was sent per day in average.
This project is building an Android app which displays and calculates the sentiment value for each tweet. The goal is to achieve a competitive result with [Sentiment140 API](http://help.sentiment140.com/api).

This application is using [Twitter Streaming API](https://dev.twitter.com/docs/streaming-apis). The json files are parsed using [GSON](http://code.google.com/p/google-gson/) library.

<<<<<<< HEAD
How to run this project?
========================
  Environment: Android SDK, Android Emulator 4.1
  Help on List View: http://www.youtube.com/watch?feature=player_embedded&v=wDBM6wVEO70#!
=======
# Related Work
Several websites have already implemented this idea. The top two from Google search results are [Sentiment140](http://www.sentiment140.com/) and [TweetFeel](http://www.tweetfeel.com/).

# Method
At first we compile the sentiment data into one single file. The sentiment data include two different categories, positive and negative word. When the classifier initializes, it loads all the sentiment data into the memory.
For each incoming tweet from Twitter Stream API, the classifier looks up each word in the tweet and compares with the sentiment data. If the word appears in the positive category, the classifier increase the sentiment total for the tweet; otherwise, if the word appears in the negative category, it decreases the sentiment total. After the counting, the classifier calculates the sentiment average by dividing the sentiment total with the number of sentiment words.
In the end, the sentiment average is the indicator for determining the relative sentiment between all those tweets.

# Evaluation
Compare our result with Sentiment140 result (API available). List the differences and let human judge which one is more accurate.

# Result

# References
## Definitions
Sentiment analysis - Wikipedia, the free encyclopedia
<http://en.wikipedia.org/wiki/Sentiment_analysis>

## Dataset
Sentiment data from several authorities
<https://github.com/jperla/sentiment-data>

## Projects
Twitter Sentiment Analysis Tutorial
Datasets, papers, presentations
<https://github.com/jeffreybreen/twitter-sentiment-analysis-tutorial-201107>

Sentiment - Nodejs implementation for text sentiment analysis
Very simply; use AFINN as classifier
<https://github.com/thisandagain/sentiment>

Troll - Node.js and neural network implementation for sentiment analysis
Need Redis, basically no training data
<https://github.com/thisandagain/troll>

Twitter sentiment analysis using Python and NLTK | Laurent Luce's Blog
<http://www.laurentluce.com/posts/twitter-sentiment-analysis-using-python-and-nltk/>

## Studies
Effective Listings of Function Stop words for Twitter
Finding Twitter Stop Words
<http://arxiv.org/abs/1205.6396>

Twitter Sentiment Analysis - AFINN
<http://fnielsen.posterous.com/afinn-a-new-word-list-for-sentiment-analysis>

Opinion mining from noisy text data
<http://dl.acm.org.libaccess.sjlibrary.org/citation.cfm?id=1390763&dl=GUIDE&coll=GUIDE>

CyberEmotions - A large-scale collective emotion research for e-communities.
<http://en.wikipedia.org/wiki/CyberEmotions>

Mining and summarizing customer reviews
<http://dl.acm.org/citation.cfm?id=1014073>

Opinion observer
<http://dl.acm.org/citation.cfm?id=1060797>

Sentiment Analysis and Subjectivity
A comprehensive tutorial for text sentiment analysis methods. It’s highly recommended to read.
<http://dl.acm.org/citation.cfm?id=1218990>
>>>>>>> cba73de3b7709ff07b90a81793003c720eac64ed
