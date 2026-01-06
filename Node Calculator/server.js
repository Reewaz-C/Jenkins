const express = require("express");
const app = express();

app.use(express.json());
app.use(express.static("public"));

app.post("/calculate", (req, res) => {
  const { num1, num2, operator } = req.body;

  let result;

  switch (operator) {
    case "+":
      result = num1 + num2;
      break;
    case "-":
      result = num1 - num2;
      break;
    case "*":
      result = num1 * num2;
      break;
    case "/":
      result = num2 !== 0 ? num1 / num2 : "Cannot divide by zero";
      break;
    default:
      result = "Invalid operator";
  }

  res.json({ result });
});

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
