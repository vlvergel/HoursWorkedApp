# Horas Trabajadas App

Esta aplicación implementa una base de datos para registrar los horarios de un **Plan de Estudios**. Se recomienda prestar especial atención al campo de **horas trabajadas por docente** al realizar una búsqueda.

Según la cantidad de horas trabajadas por un docente, se mostrará un mensaje en una nueva _Activity_ con la siguiente lógica:

- Si el docente ha trabajado **más de 40 horas**, se indicará que ha _superado lo establecido por la Ley_.
- Si ha trabajado **menos de 8 horas**, se indicará que ha _trabajado lo mínimo permitido por la Ley_.
- Si ha trabajado **entre 8 y 40 horas**, se indicará que ha _cumplido con las horas conforme a la Ley_.
