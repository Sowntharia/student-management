(function () {
  const apiBase = '/api/students';

  const form = document.getElementById('form_activate');
  const addNewBtn = document.getElementById('add-new');
  const saveBtn = document.getElementById('save-student');

  const idInput = document.getElementById('studentId');
  const firstNameInput = document.getElementById('firstName');
  const lastNameInput = document.getElementById('lastName');
  const emailInput = document.getElementById('email');

  const tableBody = document.getElementById('studentBody');
  const table = document.getElementById('studentTable');

  // ---------- helpers ----------
  function showForm(student) {
    form.style.display = 'block';
    if (student) {
      idInput.value = student.id || '';
      firstNameInput.value = student.firstName || '';
      lastNameInput.value = student.lastName || '';
      emailInput.value = student.email || '';
    } else {
      idInput.value = '';
      firstNameInput.value = '';
      lastNameInput.value = '';
      emailInput.value = '';
    }
    firstNameInput.focus();
  }

  function hideForm() {
    form.style.display = 'none';
  }

  function rowHtml(s) {
    // ensure <tr id="..."> so Selenium can reuse the same row after update
    return `
      <tr id="${s.id}">
        <td class="first">${escapeHtml(s.firstName)}</td>
        <td class="last">${escapeHtml(s.lastName)}</td>
        <td class="email">${escapeHtml(s.email)}</td>
        <td>
          <button type="button" class="btn btn-sm btn-primary edit" data-id="${s.id}">Edit</button>
          <button type="button" class="btn btn-sm btn-danger delete" data-id="${s.id}">Delete</button>
        </td>
      </tr>`;
  }

  function renderList(students) {
    tableBody.innerHTML = students.map(rowHtml).join('');
  }

  function appendRow(student) {
    tableBody.insertAdjacentHTML('beforeend', rowHtml(student));
  }

  function updateRow(student) {
    const tr = document.getElementById(String(student.id));
    if (!tr) {
      // if somehow not present, append it
      appendRow(student);
      return;
    }
    tr.querySelector('.first').textContent = student.firstName;
    tr.querySelector('.last').textContent = student.lastName;
    tr.querySelector('.email').textContent = student.email;
  }

  function removeRow(id) {
    const tr = document.getElementById(String(id));
    if (tr && tr.parentNode) tr.parentNode.removeChild(tr);
  }

  function escapeHtml(s) {
    return String(s ?? '')
      .replaceAll('&','&amp;')
      .replaceAll('<','&lt;')
      .replaceAll('>','&gt;')
      .replaceAll('"','&quot;')
      .replaceAll("'",'&#39;');
  }

  // ---------- API calls ----------
  async function listStudents() {
    const res = await fetch(apiBase);
    if (!res.ok) throw new Error('Failed to load students');
    return res.json();
  }

  async function createStudent(payload) {
    const res = await fetch(apiBase, {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error('Create failed');
    return res.json();
  }

  async function getStudent(id) {
    const res = await fetch(`${apiBase}/${id}`);
    if (!res.ok) throw new Error('Get failed');
    return res.json();
  }

  async function updateStudent(id, payload) {
    const res = await fetch(`${apiBase}/${id}`, {
      method: 'PUT',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error('Update failed');
    return res.json();
  }

  async function deleteStudent(id) {
    const res = await fetch(`${apiBase}/${id}`, { method: 'DELETE' });
    if (!res.ok && res.status !== 204) throw new Error('Delete failed');
  }

  // ---------- event wiring ----------
  addNewBtn.addEventListener('click', () => showForm(null));

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = idInput.value.trim();
    const payload = {
      firstName: firstNameInput.value.trim(),
      lastName:  lastNameInput.value.trim(),
      email:     emailInput.value.trim()
    };

    try {
      if (id) {
        const updated = await updateStudent(id, payload);
        updateRow(updated);
      } else {
        const created = await createStudent(payload);
        appendRow(created); // Selenium expects the new row at the end
      }
      hideForm();
    } catch (err) {
      console.error(err);
      alert('Save failed');
    }
  });

  // delegate clicks for edit/delete
tableBody.addEventListener('click', async (e) => {
  const btn = e.target.closest('button');
  if (!btn) return;
  const id = btn.dataset.id;

  if (btn.classList.contains('edit')) {
    // E2E wants #studentId set quickly; set it before the fetch
    idInput.value = id;
    showForm({ id }); // show the form immediately so fields are interactable

    try {
      const s = await getStudent(id); // fill actual values
      showForm(s);
    } catch (err) {
      console.error(err);
      alert('Failed to load student');
    }
  } else if (btn.classList.contains('delete')) {
    try {
      await deleteStudent(id);
      removeRow(id);
    } catch (err) {
      console.error(err);
      alert('Delete failed');
    }
  }
});


  // ---------- init ----------
  (async function init() {
    try {
      const students = await listStudents();
      renderList(students);
    } catch (err) {
      console.error(err);
    }
  })();
})();
