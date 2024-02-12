var jwt = require('jsonwebtoken')

module.exports.verificaAcesso = function (req, res, next){
    var myToken = req.query.tkn || req.body.tkn
    if (!myToken) {
      return res.status(401).jsonp({ error: "Token inexistente!" });
    }
  
    jwt.verify(myToken, "BasesJuridicas", function(err, payload) {
      if (err) {
        return res.status(401).jsonp({ error: err });
      }
      next();
    });
  }