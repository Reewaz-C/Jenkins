async function calculate() {
  const num1 = Number(document.getElementById("num1").value);
  const num2 = Number(document.getElementById("num2").value);
  const operator = document.getElementById("operator").value;

  const response = await fetch("/calculate", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ num1, num2, operator })
  });

  const data = await response.json();
  document.getElementById("result").innerText = "Result: " + data.result;
}
