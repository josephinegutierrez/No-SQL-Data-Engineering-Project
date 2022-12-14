var mongoose = require('mongoose');
mongoose.Promise = require('bluebird');
mongoose.connect('mongodb://127.0.0.1/stackoverflow', function(err)
{
  if (err)
    console.log(err);
  else
    console.log('Connected to 127.0.0.1/stackoverflow');
});
mongoose.set('debug', true);
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error: '));

var Badges = mongoose.model('Badges', require('./app/models/BadgesSchema'));
var Comments = mongoose.model('Comments', require('./app/models/CommentsSchema'));
var Posts = mongoose.model('Posts', require('./app/models/PostsSchema'));
var Votes = mongoose.model('Votes', require('./app/models/VotesSchema'));
var Users = mongoose.model('Users', require('./app/models/UsersSchema'));
var Postlinks = mongoose.model('Postlinks', require('./app/models/PostlinksSchema'));
var Tags = mongoose.model('Tags', require('./app/models/TagsSchema'));

var N_BADGES = 25000;
var N_COMMENTS = 25000;
var N_POSTLINKS = 25000;
var N_TAGS = 48373;
var N_POSTS = 25000;
var N_USERS = 25000;
var N_VOTES = 25000;

//testCheckConsistency();
//testDuplicateDb();
testAddErrorAndCheck();

function testCheckConsistency()
{
  Badges.find(function(err, result)
  {
    if (err)
      return console.error(err);

    if (result.length !== N_BADGES)
      console.error("Error: There should be " + N_BADGES + " Badges instances, but there are " + result.length + ".");

    testCollection(result, "Badges");
  }).limit(N_BADGES);

  Comments.find(function(err, result)
  {
    if (err)
      return console.error(err);

    if (result.length !== N_COMMENTS)
      console.error("Error: There should be " + N_COMMENTS + " Comments instances, but there are " + result.length + ".");

    testCollection(result, "Comments");
  }).limit(N_COMMENTS);

  Postlinks.find(function(err, result)
  {
    if (err)
      return console.error(err);

    if (result.length !== N_POSTLINKS)
      console.error("Error: There should be " + N_POSTLINKS + " Postlinks instances, but there are " + result.length + ".");

    testCollection(result, "Postlinks");
  }).limit(N_POSTLINKS);

  Posts.find(function(err, result)
  {
    if (err)
      return console.error(err);

    if (result.length !== N_POSTS)
      console.error("Error: There should be " + N_POSTS + " Posts instances, but there are " + result.length + ".");

    testCollection(result, "Posts");
  }).limit(N_POSTS);

  Users.find(function(err, result)
  {
    if (err)
      return console.error(err);

    if (result.length !== N_USERS)
      console.error("Error: There should be " + N_USERS + " Users instances, but there are " + result.length + ".");

    testCollection(result, "Users");
  }).limit(N_USERS);

  Tags.find(function(err, result)
  {
    if (err)
      return console.error(err);

    if (result.length !== N_TAGS)
      console.error("Error: There should be " + N_TAGS + " Tags instances, but there are " + result.length + ".");

    testCollection(result, "Tags");
  }).limit(N_TAGS);

  Votes.find(function(err, result)
  {
    if (err)
      return console.error(err);

    if (result.length !== N_VOTES)
      console.error("Error: There should be " + N_VOTES + " Votes instances, but there are " + result.length + ".");

    testCollection(result, "Votes");
  }).limit(N_VOTES);
}

function testDuplicateDb()
{
  Badges.find(function(err, result)
  {
    if (err)
      return console.error(err);

    result.forEach(function(item)
    {
      var newB = new Badges(); newB._id = item._id + "_TEST"; newB.Class = item.Class; newB.Date = item.Date; newB.Name = item.Name;
      newB.TagBased = item.TagBased; newB.UserId = item.UserId;
      newB.save();
    });
  }).limit(N_BADGES);

  Comments.find(function(err, result)
  {
    if (err)
      return console.error(err);

    result.forEach(function(item)
    {
      var newC = new Comments(); newC._id = item._id + "_TEST"; newC.CreationDate = item.CreationDate; newC.PostId = item.PostId; newC.Score = item.Score;
      newC.Text = item.Text; newC.UserDisplayName = item.UserDisplayName; newC.UserId = item.UserId;
      newC.save();
    });
  }).limit(N_COMMENTS);

  Postlinks.find(function(err, result)
  {
    if (err)
      return console.error(err);

    result.forEach(function(item)
    {
      var newPl = new Postlinks(); newPl._id = item._id + "_TEST"; newPl.CreationDate = item.CreationDate; newPl.LinkTypeId = item.LinkTypeId; newPl.PostId = item.PostId;
      newPl.RelatedPostId = item.RelatedPostId;
      newPl.save();
    });
  }).limit(N_POSTLINKS);

  Posts.find(function(err, result)
  {
    if (err)
      return console.error(err);

    result.forEach(function(item)
    {
      var newP = new Posts(); newP._id = item._id + "_TEST"; newP.AcceptedAnswerId = item.AcceptedAnswerId; newP.AnswerCount = item.AnswerCount; newP.Body = item.Body;
      newP.ClosedDate = item.ClosedDate; newP.CommentCount = item.CommentCount; newP.CommunityOwnedDate = item.CommunityOwnedDate; newP.CreationDate = item.CreationDate;
      newP.FavoriteCount = item.FavoriteCount; newP.LastActivityDate = item.LastActivityDate; newP.LastEditDate = item.LastEditDate; newP.LastEditorDisplayName = item.LastEditorDisplayName;
      newP.LastEditorUserId = item.LastEditorUserId; newP.OwnerDisplayName = item.OwnerDisplayName; newP.OwnerUserId = item.OwnerUserId; newP.ParentId = item.ParentId;
      newP.PostTypeId = item.PostTypeId; newP.Score = item.Score; newP.Tags = item.Tags; newP.Title = item.Title; newP.ViewCount = item.ViewCount;
      newP.save();
    });
  }).limit(N_POSTS);

  Users.find(function(err, result)
  {
    if (err)
      return console.error(err);

    result.forEach(function(item)
    {
      var newU = new Users(); newU._id = item._id + "_TEST"; newU.AboutMe = item.AboutMe; newU.AccountId = item.AccountId; newU.Age = item.Age;
      newU.CreationDate = item.CreationDate; newU.DisplayName = item.DisplayName; newU.DownVotes = item.DownVotes; newU.LastAccessDate = item.LastAccessDate;
      newU.Location = item.Location; newU.ProfileImageUrl = item.ProfileImageUrl; newU.Reputation = item.Reputation; newU.UpVotes = item.UpVotes;
      newU.Views = item.Views; newU.WebsiteUrl = item.WebsiteUrl;
      newU.save();
    });
  }).limit(N_USERS);

  Tags.find(function(err, result)
  {
    if (err)
      return console.error(err);

    result.forEach(function(item)
    {
      var newT = new Tags(); newT._id = item._id + "_TEST"; newT.Count = item.Count; newT.ExcerptPostId = item.ExcerptPostId; newT.TagName = item.TagName;
      newT.WikiPostId = item.WikiPostId;
      newT.save();
    });
  }).limit(N_TAGS);

  Votes.find(function(err, result)
  {
    if (err)
      return console.error(err);

    result.forEach(function(item)
    {
      var newV = new Votes(); newV._id = item._id + "_TEST"; newV.BountyAmount = item.BountyAmount; newV.CreationDate = item.CreationDate; newV.PostId = item.PostId;
      newV.UserId = item.UserId; newV.VoteTypeId = item.VoteTypeId;
      newV.save();
    });
  }).limit(N_VOTES);
}

function testAddErrorAndCheck()
{
  console.log("Starting TestAddErrorAndCheck...");

  var user1 = new Users(); user1._id = (new mongoose.Types.ObjectId).toString(); user1.CreationDate = "date1"; user1.DisplayName = "display1";
  user1.DownVotes = 1; user1.LastAccessDate = "accessdate1"; user1.Reputation = 1; user1.UpVotes = 1; user1.Views = "views1";

  if (user1.validateSync() !== undefined)
    console.error("Error: user1 was not correctly validated.");

  var badge1 = new Badges(); badge1._id = (new mongoose.Types.ObjectId).toString();
  var badge2 = new Badges(); badge2._id = (new mongoose.Types.ObjectId).toString(); badge2.Class = 2;
  var badge3 = new Badges(); badge3._id = (new mongoose.Types.ObjectId).toString(); badge3.Class = 3; badge3.Date = "date3";
  var badge4 = new Badges(); badge4._id = (new mongoose.Types.ObjectId).toString(); badge4.Class = 4; badge4.Date = "date4"; badge4.Name = "name4";
  var badge5 = new Badges(); badge5._id = (new mongoose.Types.ObjectId).toString(); badge5.Class = 5; badge5.Date = "date5"; badge5.Name = "name5"; badge5.TagBased = "tagbased5";
  var badge6 = new Badges(); badge6._id = (new mongoose.Types.ObjectId).toString();
  badge6.Class = 6; badge6.Date = "date6"; badge6.Name = "name6"; badge6.TagBased = "tagbased6"; badge6.UserId = user1;

  if (badge1.validateSync() === undefined || Object.values(badge1.validateSync().errors).length !== 5)
    console.error("Error: badge1 was not correctly validated.");
  if (badge2.validateSync() === undefined || Object.values(badge2.validateSync().errors).length !== 4)
    console.error("Error: badge2 was not correctly validated.");
  if (badge3.validateSync() === undefined || Object.values(badge3.validateSync().errors).length !== 3)
    console.error("Error: badge3 was not correctly validated.");
  if (badge4.validateSync() === undefined || Object.values(badge4.validateSync().errors).length !== 2)
    console.error("Error: badge4 was not correctly validated.");
  if (badge5.validateSync() === undefined || Object.values(badge5.validateSync().errors).length !== 1)
    console.error("Error: badge5 was not correctly validated.");
  if (badge6.validateSync() !== undefined)
    console.error("Error: badge6 was not correctly validated.");

  console.log("TestAddErrorAndCheck finished.");
}

function testCollection(collection, tableName)
{
  if (collection === null || collection.length === 0)
    return;

  console.log("Checking consistency of the \"" + tableName + "\" collection");
  var errorNumber = 0;

  collection.forEach(function(obj)
  {
    var validation = obj.validateSync();
    if (typeof validation !== "undefined")
    {
      console.error(validation);
      errorNumber++;
    }
  });

  if (errorNumber)
    console.error("-->\"" + tableName + "\" collection: " + errorNumber + " errors found");
  else
    console.log("-->\"" + tableName + "\" collection: No errors found!");
}